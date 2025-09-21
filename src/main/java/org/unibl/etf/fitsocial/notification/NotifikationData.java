package org.unibl.etf.fitsocial.notification;

import java.util.Map;

public record NotifikationData(String title, String body, String imageUrl, Map<String, String> data){

}
