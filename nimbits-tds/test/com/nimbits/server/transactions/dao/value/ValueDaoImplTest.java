/*
 * Copyright (c) 2010 Nimbits Inc.
 *
 * http://www.nimbits.com
 *
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/gpl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the license is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, eitherexpress or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.nimbits.server.transactions.dao.value;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.files.*;
import com.nimbits.client.enums.EntityType;
import com.nimbits.client.enums.ProtectionLevel;
import com.nimbits.client.exception.NimbitsException;
import com.nimbits.client.model.common.CommonFactoryLocator;
import com.nimbits.client.model.entity.EntityModelFactory;
import com.nimbits.client.model.entity.EntityName;
import com.nimbits.client.model.point.Point;
import com.nimbits.client.model.point.PointModelFactory;
import com.nimbits.client.model.value.Value;
import com.nimbits.client.model.value.impl.ValueFactory;
import com.nimbits.client.model.valueblobstore.ValueBlobStore;
import com.nimbits.server.NimbitsServletTest;
import com.nimbits.server.time.TimespanServiceFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.*;
import java.util.zip.DataFormatException;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.*;

/**
 * Created by Benjamin Sautner
 * User: bsautner
 * Date: 3/22/12
 * Time: 12:00 PM
 */
public class ValueDaoImplTest extends NimbitsServletTest {




    double total = 0.0;
    private  List<Value> loadSomeData() {
        List<Value> values = new ArrayList<Value>();
        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            Value v = ValueFactory.createValueModel(r.nextDouble());
            total += v.getDoubleValue();
            values.add(v);
        }
        return values;
    }




    @Test
    public void testConsolidateDate() throws NimbitsException {
        Date zero = TimespanServiceFactory.getInstance().zeroOutDate(new Date());
        for (int i = 1; i < 11; i++) {
            List<Value> values = new ArrayList<Value>(3);
            values.add(ValueFactory.createValueModel(1));
            values.add(ValueFactory.createValueModel(1));
            values.add(ValueFactory.createValueModel(1));
            valueDao.recordValues(values);
            assertEquals(i, valueDao.getAllStores().size());
        }


        valueDao.consolidateDate(zero);
        assertEquals(1, valueDao.getAllStores().size());

        List<Value> result = valueDao.getTopDataSeries(100);
        double total = 0.0;
        for (Value v : result) {
            total += v.getDoubleValue();

        }
        assertEquals(30.0, total, 0.0);

    }

    @Test
    public void testMissingBlobRecovery() throws NimbitsException, FileNotFoundException {
        Date zero = TimespanServiceFactory.getInstance().zeroOutDate(new Date());
        String key = null;
        final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        for (int i = 1; i < 11; i++) {
            List<Value> values = new ArrayList<Value>(3);
            values.add(ValueFactory.createValueModel(1));
            values.add(ValueFactory.createValueModel(1));
            values.add(ValueFactory.createValueModel(1));
            List<ValueBlobStore> d = valueDao.recordValues(values);
            assertFalse(d.isEmpty());
            assertEquals(i, valueDao.getAllStores().size());
            key = d.get(0).getBlobKey();
        }

        blobstoreService.delete(new BlobKey(key));

        valueDao.consolidateDate(zero);
        assertEquals(1, valueDao.getAllStores().size());

        List<Value> result = valueDao.getTopDataSeries(100);
        double total = 0.0;
        for (Value v : result) {
            total += v.getDoubleValue();

        }
        assertEquals(27.0, total, 0.0);

    }

    @Test
    public void testGetBlobStoreByBlobKey() throws NimbitsException {
        Date zero = TimespanServiceFactory.getInstance().zeroOutDate(new Date());
        final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

        List<Value> values = new ArrayList<Value>(3);
        values.add(ValueFactory.createValueModel(1));
        values.add(ValueFactory.createValueModel(2));
        values.add(ValueFactory.createValueModel(3));
        List<ValueBlobStore> d = valueDao.recordValues(values);
        assertFalse(d.isEmpty());

        String key = d.get(0).getBlobKey();

        List<ValueBlobStore> v =   valueDao.getBlobStoreByBlobKey(new BlobKey(key));
        assertFalse(v.isEmpty());


        EntityName name = CommonFactoryLocator.getInstance().createName("f", EntityType.file);
        com.nimbits.client.model.entity.Entity e = EntityModelFactory.createEntity(name, "", EntityType.file,
                ProtectionLevel.everyone,user.getKey(), user.getKey() );



    }


    @Test
    public void testBlobStore() throws IOException {
        // Get a file service
        FileService fileService = FileServiceFactory.getFileService();

        // Create a new Blob file with mime-type "text/plain"
        AppEngineFile file = fileService.createNewBlobFile("text/plain");

        // Open a channel to write to it
        boolean lock = false;
        FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);

        // Different standard Java ways of writing to the channel
        // are possible. Here we use a PrintWriter:
        PrintWriter out = new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
        out.println("The woods are lovely dark and deep.");
        out.println("But I have promises to keep.");

        // Close without finalizing and save the file path for writing later
        out.close();
        String path = file.getFullPath();

        // Write more to the file in a separate request:
        file = new AppEngineFile(path);

        // This time lock because we intend to finalize
        lock = true;
        writeChannel = fileService.openWriteChannel(file, lock);

        // This time we write to the channel directly
        writeChannel.write(ByteBuffer.wrap
                ("And miles to go before I sleep.".getBytes()));

        // Now finalize
        writeChannel.closeFinally();

        // Later, read from the file using the file API
        lock = false; // Let other people read at the same time
        FileReadChannel readChannel = fileService.openReadChannel(file, false);

        // Again, different standard Java ways of reading from the channel.
        BufferedReader reader =
                new BufferedReader(Channels.newReader(readChannel, "UTF8"));
        String line = reader.readLine();
        // line = "The woods are lovely dark and deep."

        readChannel.close();

        // Now read from the file using the Blobstore API
        BlobKey blobKey = fileService.getBlobKey(file);
        BlobstoreService blobStoreService = BlobstoreServiceFactory.getBlobstoreService();
        String segment = new String(blobStoreService.fetchData(blobKey, 30, 40));


        assertNotNull(blobKey);
        assertEquals(line, "The woods are lovely dark and deep." );
    }
    @Test
    public void testBlobStoreWithCompression() throws IOException, DataFormatException {
//        String sample =  "hello compression";
//        BlobKey dataBlobKey =
//                BlobStoreImpl.putInBlobStore("MULTIPART_FORM_DATA",
//                        CompressionImpl.compress(sample.getBytes()));
//        System.out.println(dataBlobKey);


    }


    @Test
    public void testGetTopDataSeries(){
        List<Value> values = loadSomeDataOverDays();


        try {
            valueDao.recordValues(values);
            List<Value> result = valueDao.getTopDataSeries(10);
            assertEquals(10, result.size());
        } catch (NimbitsException e) {
            e.printStackTrace();
            fail();
        }


    }
    @Test
    public void testExpireData() throws NimbitsException {
        final List<Value> values = new ArrayList<Value>(90);
        for (int i = 10; i < 100; i++) {
            final Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DATE, -1 * i);
            final Double d1 = (double) i;
            final Value v1 = ValueFactory.createValueModel(d1, c1.getTime());
            values.add(v1);
        }
        valueDao.recordValues(values);

        List<Value> result1 = valueDao.getTopDataSeries(90);
        assertEquals(90, result1.size());

        final List<Value> values2 = new ArrayList<Value>(5);
        for (int i = 0; i < 5; i++) {
            final Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DATE, -1 * i);
            final Double d1 = (double) i;
            final Value v1 = ValueFactory.createValueModel(d1, c1.getTime());
            values2.add(v1);
        }

        valueDao.recordValues(values2);
        valueDao.deleteExpiredData();


        List<Value> result = valueDao.getTopDataSeries(10);
            assertEquals(5, result.size());
    }
    @Test
    public void testGetRecordedValuePrecedingTimestamp() {
        final List<Value> values = loadSomeDataOverDays();


        final ValueDAOImpl dao = new ValueDAOImpl(point);
        try {
            dao.recordValues(values);
            List<Value> all = dao.getTopDataSeries(1000);
            assertEquals(100, all.size());
            for (int i = 0; i < 100; i++) {
                final Calendar c1 = Calendar.getInstance();
                c1.add(Calendar.DATE, -1 * i);
                final Double d1 = (double) i;
                final Value vx = dao.getRecordedValuePrecedingTimestamp(c1.getTime());
                assertEquals(d1, vx.getDoubleValue(), 0.0);
            }
        } catch (NimbitsException e) {
            fail();
            e.printStackTrace();
        }

    }



    @Test
    public void testGetRecordedValuePrecedingTimestampMultiplePoints() throws NimbitsException {
        final List<Value> values = loadSomeDataOverDays();
        EntityName name1 = CommonFactoryLocator.getInstance().createName("1", EntityType.point);
        EntityName name2 = CommonFactoryLocator.getInstance().createName("1", EntityType.point);
        EntityName name3 = CommonFactoryLocator.getInstance().createName("1", EntityType.point);
        com.nimbits.client.model.entity.Entity entity1 = EntityModelFactory.createEntity(name1, "", EntityType.point, ProtectionLevel.everyone, "", "");
        com.nimbits.client.model.entity.Entity entity2 = EntityModelFactory.createEntity(name2, "", EntityType.point, ProtectionLevel.everyone, "", "");
        com.nimbits.client.model.entity.Entity entity3 = EntityModelFactory.createEntity(name3, "", EntityType.point, ProtectionLevel.everyone, "", "");


        final Point point1 = PointModelFactory.createPointModel(entity1);
        final Point point2 = PointModelFactory.createPointModel(entity2);
        final Point point3 = PointModelFactory.createPointModel(entity3);

        final ValueDAOImpl dao1 = new ValueDAOImpl(point1);
        final ValueDAOImpl dao2 = new ValueDAOImpl(point2);
        final ValueDAOImpl dao3 = new ValueDAOImpl(point3);


        try {
            dao1.recordValues(values);
            dao2.recordValues(values);
            dao3.recordValues(values);

            for (int i = 0; i < 100; i++) {
                final Calendar c1 = Calendar.getInstance();
                c1.add(Calendar.DATE, -1 * i);
                final Double d1 = (double) i;
                final Value vx = dao1.getRecordedValuePrecedingTimestamp(c1.getTime());
                assertEquals(d1, vx.getDoubleValue(), 0.0);
            }
            for (int i = 0; i < 100; i++) {
                final Calendar c1 = Calendar.getInstance();
                c1.add(Calendar.DATE, -1 * i);
                final Double d1 = (double) i;
                final Value vx = dao2.getRecordedValuePrecedingTimestamp(c1.getTime());
                assertEquals(d1, vx.getDoubleValue(), 0.0);
            }
            for (int i = 0; i < 100; i++) {
                final Calendar c1 = Calendar.getInstance();
                c1.add(Calendar.DATE, -1 * i);
                final Double d1 = (double) i;
                final Value vx = dao3.getRecordedValuePrecedingTimestamp(c1.getTime());
                assertEquals(d1, vx.getDoubleValue(), 0.0);
            }
        } catch (NimbitsException e) {
            fail();
            e.printStackTrace();
        }

    }


    private static List<Value> loadSomeDataOverDays() {
        final List<Value> values = new ArrayList<Value>(100);
        for (int i = 0; i < 100; i++) {
            final Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DATE, -1 * i);
            final Double d1 = (double) i;
            final Value v1 = ValueFactory.createValueModel(d1, c1.getTime());
            values.add(v1);
        }
        return values;
    }


    @Test
    public void testRecordValues() {

        List<Value> values = loadSomeData();

        ValueDAOImpl dao = new ValueDAOImpl(point);
        try {
            dao.recordValues(values);
            List<Value> result = dao.getTopDataSeries(100);
            assertNotNull(result);
            assertEquals(result.size(), 10);
            double ret = 0.0;
            for (Value v : result) {
                ret += v.getDoubleValue();
            }
            assertEquals(total, ret, 0.0);


        } catch (NimbitsException e) {

            fail();
        }
    }

    @Test
    @Ignore
    public void testRecordValuesLoad() {
        long s = new Date().getTime();

        for (int i = 0; i < 1000; i++) {
            List<Value> values = loadSomeData();

            ValueDAOImpl dao = new ValueDAOImpl(point);
            try {
                dao.recordValues(values);
                List<Value> result = dao.getTopDataSeries(100);
                assertNotNull(result);

                double ret = 0.0;
                for (Value v : result) {
                    ret += v.getDoubleValue();
                }

            } catch (NimbitsException e) {

                fail();
            }
        }

    }


    // run this test twice to prove we're not leaking any state across tests
    private void doTest() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        assertEquals(0, ds.prepare(new Query("yam")).countEntities(withLimit(10)));
        ds.put(new Entity("yam"));
        ds.put(new Entity("yam"));
        assertEquals(2, ds.prepare(new Query("yam")).countEntities(withLimit(10)));
    }



    @Test
    public void testInsert1() {
        doTest();
    }

    @Test
    public void testInsert2() {
        doTest();
    }
    @Test
    public void testZeroOutDate() {
        Calendar now = Calendar.getInstance();
        Calendar midnightAm = Calendar.getInstance();
        midnightAm.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), 0, 0, 0);
        midnightAm.add(Calendar.MILLISECOND, now.get(Calendar.MILLISECOND) * -1);
        Date zero = TimespanServiceFactory.getInstance().zeroOutDate(now.getTime());
        assertEquals(midnightAm.getTime(), zero);
    }




}
