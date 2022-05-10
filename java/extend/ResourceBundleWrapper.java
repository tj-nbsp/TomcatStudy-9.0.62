package extend;

import org.apache.catalina.startup.VersionLoggerListener;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class ResourceBundleWrapper extends ResourceBundle {

    private static final Log log = LogFactory.getLog(ResourceBundleWrapper.class);

    private ResourceBundle delegate;

    public ResourceBundleWrapper(ResourceBundle resourceBundle) {
        this.delegate = resourceBundle;
    }

    @Override
    public Locale getLocale() {
        return delegate.getLocale();
    }

    @Override
    protected Object handleGetObject(String key) {
        Object result = null;
        try {
            Method method = ResourceBundle.class.getDeclaredMethod("handleGetObject", String.class);
            method.setAccessible(true);
            result = method.invoke(delegate, key);
            if (result instanceof String && delegate.getLocale().getLanguage().equals(Locale.CHINESE.getLanguage())) {
                result = new String( ((String)result).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // ignore
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Enumeration<String> getKeys() {
        return delegate.getKeys();
    }

    @Override
    public String getBaseBundleName() {
        return delegate.getBaseBundleName();
    }

    @Override
    public Set<String> keySet() {
        return delegate.keySet();
    }

}
