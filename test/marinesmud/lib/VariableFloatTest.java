/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.lib;

import pl.jblew.code.jutils.utils.math.VariableFloat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jblew
 */
public class VariableFloatTest {

    public VariableFloatTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getValue method, of class VariableFloat.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        VariableFloat instance = null;
        float expResult = 0.0F;
        float result = instance.getValue();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class VariableFloat.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        float f = 0.0F;
        VariableFloat instance = null;
        float expResult = 0.0F;
        float result = instance.add(f);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of subtract method, of class VariableFloat.
     */
    @Test
    public void testSubtract() {
        System.out.println("subtract");
        float f = 0.0F;
        VariableFloat instance = null;
        float expResult = 0.0F;
        float result = instance.subtract(f);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}