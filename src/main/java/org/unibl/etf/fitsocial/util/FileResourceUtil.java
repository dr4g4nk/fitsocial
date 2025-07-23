package org.unibl.etf.fitsocial.util;

import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.unibl.etf.fitsocial.service.FileStorageService;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileResourceUtil {
private final FileStorageService fileStorageService;
    public FileResourceUtil(FileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
    }

    private static final int BUFFER_SIZE = 64 * 1024; // 64 KB

    public  ResourceResponse getResourceResponse(String uri, String mimeType, List<HttpRange> ranges, int bufferSize) throws IOException {
        Resource resource = fileStorageService.loadAsResource(uri);
        long fileSize = resource.contentLength();

        MediaType mediaType = MediaType.parseMediaType(mimeType);

        boolean isVideo = mediaType.getType().equals("video");
        long start, end = fileSize - 1;
        HttpStatus status = HttpStatus.OK;

        if (isVideo && !ranges.isEmpty()) {
            HttpRange range = ranges.getFirst();
            start = range.getRangeStart(fileSize);
            end = range.getRangeEnd(fileSize);
            status = HttpStatus.PARTIAL_CONTENT;
        } else {
            start = 0;
        }

        long contentLength = end - start + 1;

        var byteBufferSize = bufferSize > 0 ? bufferSize : BUFFER_SIZE;

        StreamingResponseBody body = outputStream -> {
            try (SeekableByteChannel channel = Files.newByteChannel(resource.getFile().toPath(), StandardOpenOption.READ)) {
                channel.position(start);
                ByteBuffer buffer = ByteBuffer.allocate(byteBufferSize);
                long bytesLeft = contentLength;

                while (bytesLeft > 0) {
                    int read = channel.read(buffer);
                    if (read == -1) break;
                    buffer.flip();

                    int toWrite = (int) Math.min(read, bytesLeft);
                    outputStream.write(buffer.array(), 0, toWrite);
                    bytesLeft -= toWrite;
                    buffer.clear();
                }
            }
        };

        CacheControl cacheControl = CacheControl.maxAge(5, TimeUnit.HOURS);

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.set(HttpHeaders.CONTENT_TYPE, mediaType.toString());
        respHeaders.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        respHeaders.setContentLength(contentLength);
        respHeaders.setCacheControl(cacheControl);

        if (status == HttpStatus.PARTIAL_CONTENT) {
            respHeaders.set(
                    HttpHeaders.CONTENT_RANGE,
                    String.format("bytes %d-%d/%d", start, end, fileSize)
            );
        }

        return new ResourceResponse(body, respHeaders, status);
    }

    public class ResourceResponse{
        private StreamingResponseBody body;
        private HttpHeaders respHeaders;
        private HttpStatus status = HttpStatus.OK;

        public ResourceResponse(StreamingResponseBody body, HttpHeaders respHeaders, HttpStatus status) {
            this.body = body;
            this.respHeaders = respHeaders;
            this.status = status;
        }

        public StreamingResponseBody getBody() {
            return body;
        }

        public void setBody(StreamingResponseBody body) {
            this.body = body;
        }

        public HttpHeaders getRespHeaders() {
            return respHeaders;
        }

        public void setRespHeaders(HttpHeaders respHeaders) {
            this.respHeaders = respHeaders;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public void setStatus(HttpStatus status) {
            this.status = status;
        }
    }
}
