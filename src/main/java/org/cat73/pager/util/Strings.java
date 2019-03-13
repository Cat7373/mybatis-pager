package org.cat73.pager.util;

/**
 * 字符串工具类
 * <p><em>内部使用，不保证未来依旧存在，不建议外部使用</em></p>
 * @deprecated 考虑移除
 */
@Deprecated
public final class Strings {
    private Strings() {
        throw new UnsupportedOperationException();
    }

    /**
     * 判断字符序列是否为 null 或全部为空白字符
     *
     * @param chs 被判断的字符序列
     * @return 判断结果
     */
    public static boolean isBlank(CharSequence chs) {
        if (Strings.notEmpty(chs)) {
            for (int i = chs.length() - 1; i >= 0; i--) {
                if (!Character.isWhitespace(chs.charAt(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 判断字符序列是否不为 null 且不全部为空白字符
     *
     * @param chs 被判断的字符序列
     * @return 判断结果
     */
    public static boolean notBlank(CharSequence chs) {
        return !Strings.isBlank(chs);
    }

    /**
     * 判断字符序列是否为 null 或长度为 0
     * @param chs 被判断的字符序列
     * @return 判断结果
     */
    public static boolean isEmpty(CharSequence chs) {
        return chs == null || chs.length() == 0;
    }

    /**
     * 判断字符序列是否不为 null 且长度不为 0
     * @param chs 被判断的字符序列
     * @return 判断结果
     */
    public static boolean notEmpty(CharSequence chs) {
        return !Strings.isEmpty(chs);
    }
}
