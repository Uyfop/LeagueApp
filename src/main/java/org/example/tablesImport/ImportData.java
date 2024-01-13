//package org.example.tablesImport;
//
//import org.hibernate.Session;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.HashMap;
//import java.util.Map;
//public class ImportData {
//
//    public void importEntities(Session session, String fileName, EntityCreator creator) {
//        try {
//            InputStream inputStream = ImportData.class.getResourceAsStream(fileName);
//            if (inputStream != null) {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    String[] entityData = line.split(",");
//                    if (entityData.length > 0) {
//                        Map<String, Object> attributes = creator.createAttributes(entityData);
//                        Object entity = creator.createEntity(attributes);
//                        if(entity != null)
//                            session.save(entity);
//                    }
//                }
//                reader.close();
//            } else {
//                System.err.println("File not found");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
