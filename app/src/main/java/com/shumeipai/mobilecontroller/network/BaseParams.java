package com.shumeipai.mobilecontroller.network;

import android.text.TextUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public abstract class BaseParams {

    private static final String TAG = "BaseParams";

    protected ConcurrentHashMap< String, Object > urlParams;

    protected BaseParams() {
        this.init();
    }

    public Object get( String key ) {
        return this.urlParams != null && key != null ? this.urlParams.get( key ) : "";
    }

    protected BaseParams( Map< String, Object > source ) {
        this.init();
        Iterator iterator = source.entrySet().iterator();

        while ( iterator.hasNext() ) {
            Map.Entry entry = ( Map.Entry ) iterator.next();
            this.put( ( String ) entry.getKey(), entry.getValue() );
        }

    }

    protected BaseParams( String key, Object value ) {
        this.init();
        this.put( key, value );
    }

    protected BaseParams( Object... keysAndValues ) {
        this.init();
        int len = keysAndValues.length;
        if ( len % 2 != 0 ) {
            throw new IllegalArgumentException( "Supplied arguments must be even" );
        } else {
            for ( int i = 0; i < len; i += 2 ) {
                String key = String.valueOf( keysAndValues[i] );
                Object value = keysAndValues[i + 1];
                this.put( key, value );
            }

        }
    }

    private boolean checkValue( final Object value ) {
        if ( value instanceof CharSequence ) {
            return !TextUtils.isEmpty( ( CharSequence ) value );
        }

        if ( value == null || value.getClass() == null ) {
            return false;
        }

        final Class< ? > clazz = value.getClass();
        return clazz.isPrimitive() ||
                clazz.isAssignableFrom( Boolean.class ) ||
                clazz.isAssignableFrom( Character.class ) ||
                clazz.isAssignableFrom( Byte.class ) ||
                clazz.isAssignableFrom( Short.class ) ||
                clazz.isAssignableFrom( Integer.class ) ||
                clazz.isAssignableFrom( Long.class ) ||
                clazz.isAssignableFrom( Float.class ) ||
                clazz.isAssignableFrom( Double.class );
    }

    private boolean checkKey( String key ) {
        return !TextUtils.isEmpty( key );
    }

    public BaseParams put( String key, Object value ) {
        if ( checkKey( key ) && checkValue( value ) ) {
            urlParams.put( key, value );
        } else {
           // Logger.e( TAG, "parameter key is illegal or parameter value is illegal" );
        }
        return this;
    }

    public Object remove( String key ) {
        return this.urlParams.remove( key );
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        Iterator< Map.Entry< String, Object > > iterator = this.urlParams.entrySet().iterator();
        while ( iterator.hasNext() ) {
            Map.Entry< String, Object > entry = iterator.next();
            if ( result.length() > 0 ) {
                result.append( "&" );
            }

            result.append( entry.getKey() );
            result.append( "=" );
            result.append( entry.getValue() );
        }
        return result.toString();
    }

    private void init() {
        this.urlParams = new ConcurrentHashMap< String, Object >();
    }

    public List< BasicNameValuePair > getParamsList() {
        LinkedList< BasicNameValuePair > pairs = new LinkedList< BasicNameValuePair >();
        Iterator< Map.Entry< String, Object > > iterator = this.urlParams.entrySet().iterator();

        while ( iterator.hasNext() ) {
            Map.Entry< String, Object > entry = iterator.next();
            pairs.add( new BasicNameValuePair( entry.getKey(), entry.getValue() ) );
        }

        return pairs;
    }

    public Map< String, Object > getOriginMap() {
        return urlParams;
    }

    public String getParamString() {
        StringBuilder result = new StringBuilder();
        Iterator< BasicNameValuePair > iterator = getParamsList().iterator();

        while ( iterator.hasNext() ) {
            BasicNameValuePair parameter = iterator.next();
            String key = parameter.getName();
            Object value = parameter.getValue();
            if ( result.length() > 0 ) {
                result.append( "&" );
            }

            result.append( key );
            result.append( "=" );
            result.append( value );
        }

        return result.toString();
    }

    public static class BasicNameValuePair {
        private String name;
        private Object value;

        public BasicNameValuePair( String name, Object value ) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName( String name ) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue( Object value ) {
            this.value = value;
        }
    }

}
