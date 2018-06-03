package DAO;

import com.mongodb.*;
import com.mongodb.util.JSON;

public class MongoDB {

    private MongoClient mongoClient;
    private DB _db;
    private DBCollection _collection;

    public void initialize()
    {
        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            _db = mongoClient.getDB("test");
            _collection = _db.getCollection("dummyColl_test5");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public void insertData(String jsonStr)
        {

            try {

                // convert JSON to DBObject directly

                DBObject obj = (DBObject) JSON.parse(jsonStr);

                _collection.insert(obj);

                System.out.println("Done");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
