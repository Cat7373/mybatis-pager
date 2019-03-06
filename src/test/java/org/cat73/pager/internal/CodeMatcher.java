package org.cat73.pager.internal;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * code >= 0 的判断类
 */
public class CodeMatcher extends BaseMatcher<Object> {
    @Override
    public boolean matches(Object item) {
        if (item instanceof Number) {
            return ((Number) item).longValue() >= 0L;
        }

        if (!(item instanceof String)) {
            return false;
        }

        String str = (String) item;
        try {
            return Long.parseLong(str) >= 0L;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("code >= 0");
    }
}
