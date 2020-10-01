package com.github.playerforcehd.gcaptchavalidator.model.response;

/**
 * Contains the specified possible types.
 *
 * A type refers to the location where a ReCaptcha has been used.
 * It allows to determine which exact type is stored in the field responsible
 * to provide apk_pkg_name or hostname.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public enum ClientType {
    WEB,
    ANDROID
}
