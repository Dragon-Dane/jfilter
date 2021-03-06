package com.jfilter.components;

import com.jfilter.converter.FilterClassWrapper;
import com.jfilter.filter.FilterFields;
import com.jfilter.mock.config.WSConfigurationHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@Component
public class FilterConverterITest {
    private FilterConfiguration filterConfiguration;
    private FilterConverter filterConverter;

    @Autowired
    public FilterConverterITest setFilterConfiguration(FilterConfiguration filterConfiguration) {
        this.filterConfiguration = filterConfiguration;
        return this;
    }

    @Before
    public void init() throws Exception {
        WSConfigurationHelper.instance(WSConfigurationHelper.Instance.FILTER_ENABLED2, this);
        filterConfiguration.setEnabled(true);

        filterConverter = new FilterConverter(filterConfiguration);
    }

    @Test
    public void testCanRead() {
        boolean canRead = filterConverter.canRead(null, MediaType.APPLICATION_JSON);
        assertFalse(canRead);
    }

    @Test
    public void testRead() throws IOException {
        FilterClassWrapper wrapper = (FilterClassWrapper) filterConverter.read(null, new MockHttpInputMessage("test".getBytes()));
        assertNull(wrapper);
    }

    @Test
    public void testCanWrite() {
        boolean canWrite = filterConverter.canWrite(null, MediaType.APPLICATION_JSON);
        assertTrue(canWrite);
    }

    @Test
    public void testCanWriteFalse() {
        boolean canWrite = filterConverter.canWrite(null, new MediaType("application", "test2"));
        assertFalse(canWrite);
    }

    @Test
    public void testCanWriteDisabled() {
        filterConfiguration.setEnabled(false);
        boolean canWrite = filterConverter.canWrite(null, MediaType.APPLICATION_JSON);
        assertFalse(canWrite);
    }

    @Test
    public void testSupportsTrue() {
        filterConfiguration.setEnabled(true);
        boolean supports = filterConverter.supports(FilterFields.class);
        assertTrue(supports);
    }
}
