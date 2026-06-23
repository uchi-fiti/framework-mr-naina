package uchi.utils;

public class UrlMethod {
    private String url;
    private MethodEnum method;

    public UrlMethod(String url, MethodEnum method) {
        this.url = url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public MethodEnum getMethod() {
        return method;
    }
    public void setMethod(MethodEnum method) {
        this.method = method;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrlMethod other = (UrlMethod) obj;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        if (method != other.method)
            return false;
        return true;
    }

    

    
}
