package com.tyt.driver.ui.loginOrSign;

public class ChooseLanguage<T, V> {
    private final T icon;
    private final V languageName;

    public ChooseLanguage(T icons, V languageNames) {
        icon = icons;
        languageName = languageNames;
    }

    public T getIcon() {
        return icon;
    }

    public V getLanguageName() {
        return languageName;
    }
}
