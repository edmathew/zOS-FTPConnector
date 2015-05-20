package net.ejpm.conn.remote.zos;

import net.ejpm.conn.remote.zos.dataset.DataSet;
import net.ejpm.conn.remote.zos.dataset.DatasetFactory;
import net.ejpm.conn.remote.zos.dataset.PDSMember;
import net.ejpm.conn.remote.zos.dataset.SequentialDataSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

/**
 *
 * @author XPTA455
 */
public class TestDatasetFactory {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldGenerateASequentialDataset() {
        final String fullName = "USERID.TEST.BLAS.GET";
        final String lastName = "GET";

        final DataSet d = new DatasetFactory().createFromID(fullName);
        assertThat(d, instanceOf(SequentialDataSet.class));
        assertEquals(d.getFullDSName(), fullName);
        assertEquals(((SequentialDataSet) d).getLastName(), lastName);
    }

    @Test
    public void shouldGenerateAPDSMember() {
        final String fullName = "USERID.TEST.LOAD(LOAD001)";
        final String memberName = "LOAD001";

        final DataSet d = new DatasetFactory().createFromID(fullName);
        assertThat(d, instanceOf(PDSMember.class));
        assertEquals(d.getFullDSName(), fullName);
        assertEquals(((PDSMember) d).getMemberName(), memberName);
    }

}
