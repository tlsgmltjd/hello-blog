package com.blog.helloblog;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

public class JUnitCycleTest {
    @BeforeAll
    static void beforeAll() {
        System.out.println("@BeforeAll");
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("@beforeEach");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }

    @Test
    public void test3() {
        System.out.println("test3");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("@AfterAll");
    }

    @AfterEach
    public void afterEach() {
        System.out.println("@AfterEach");
    }
}

// @BeforeAll
// @beforeEach
// test1
// @AfterEach
// @beforeEach
// test2
// @AfterEach
// @beforeEach
// test3
// @AfterEach
// @AfterAll