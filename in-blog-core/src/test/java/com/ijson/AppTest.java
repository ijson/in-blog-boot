package com.ijson;

import org.bson.types.ObjectId;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        ObjectId objectId = new ObjectId("5d5793274737fb0705428b44");
        System.out.println(objectId.getDate());
    }
}
