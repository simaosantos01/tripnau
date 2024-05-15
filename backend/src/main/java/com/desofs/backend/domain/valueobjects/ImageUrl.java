package com.desofs.backend.domain.valueobjects;


import static org.apache.commons.lang3.Validate.*;

public class ImageUrl {

    private final String url;

    private ImageUrl(String url) {
        this.url = url;
    }

    public static ImageUrl create(String url) {
        notNull(url,
                "URL must not be null.");
        notEmpty(url.trim(),
                "URL must not be empty.");
        matchesPattern(url.trim(),
                "^(https?|http)://([a-zA-Z0-9]+\\.)*[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[a-zA-Z]{2,}(\\:[0-9]{1,5})?(\\/[a-zA-Z0-9]+)*(\\/[a-zA-Z0-9]+\\.[a-zA-Z0-9]{1,})?$", // by owasp: https://owasp.org/www-community/OWASP_Validation_Regex_Repository
                "Invalid URL format.");

        return new ImageUrl(new String(url.trim()));
    }

    public ImageUrl copy() {
        return new ImageUrl(this.url);
    }

    public String getUrl() {
        return new String(this.url);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return this.url.equals(((ImageUrl) obj).url);
    }

}