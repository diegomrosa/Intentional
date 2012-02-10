package com.diegomrosa.intentional;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.*;
import java.util.*;

final class Mime {
    private static final String TAG = Mime.class.getSimpleName();

    private static final String TYPE_FILE = "mimetypes.json";
    private static final String ITEM_SEPARATOR = ", ";
    private static final String PAIR_SEPARATOR = ": ";
    private static final char TYPE_SEPARATOR = '/';
    private static final String WILDCARD_STRING = "*";

    private static final int ESTIMATED_NUMBER_OF_EXTENSIONS = 850;
    private static final int ESTIMATED_NUMBER_OF_TYPES = 650;
    private static final double ESTIMATED_HASH_LOAD_FACTOR = 0.75;
    private static final int EXTENSION_INITIAL_CAPACITY =
        (int) (ESTIMATED_NUMBER_OF_EXTENSIONS / ESTIMATED_HASH_LOAD_FACTOR);
    private static final int TYPE_INITIAL_CAPACITY =
        (int) (ESTIMATED_NUMBER_OF_TYPES / ESTIMATED_HASH_LOAD_FACTOR);


    public class Extension {
        private String value;
        private List<Type> types;

        public Extension(String value) {
            this.value = value;
        }

        public void addType(Type type) {
            if (types == null) {
                types = new LinkedList<Type>();
            }
            types.add(type);
        }

        public List<Type> getTypes() {
            if (types == null) {
                return Collections.emptyList();
            }
            return types;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public class Type implements Comparable<Type> {
        private String value;
        private List<Extension> extensions;

        public Type(String value) {
            this.value = value;
        }

        public void addExtension(Extension type) {
            if (extensions == null) {
                extensions = new LinkedList<Extension>();
            }
            extensions.add(type);
        }

        public List<Extension> getExtensions() {
            if (extensions == null) {
                return Collections.emptyList();
            }
            return extensions;
        }

        @Override
        public String toString() {
            return value;
        }

        @Override
        public int compareTo(Type other) {
            return this.value.compareTo(other.value);
        }
    }

    private Map<String, Extension> extensionMap;
    private Map<String, Type> typeMap;
    private List<Type> typeList;

    private static Mime instance;

    public static synchronized Mime getInstance(Context context) {
        if (instance == null) {
            instance = new Mime(context);
        }
        return instance;
    }

    private Mime(Context context) {
        extensionMap = new HashMap<String, Extension>(EXTENSION_INITIAL_CAPACITY);
        typeMap = new HashMap<String, Type>(TYPE_INITIAL_CAPACITY);
        typeList = new ArrayList<Type>(ESTIMATED_NUMBER_OF_TYPES);
        loadTypes(context);
    }

    public List<Type> getTypes() {
        return typeList;
    }

    public List<Type> getTypes(String extensionString) {
        Extension extension = extensionMap.get(extensionString);

        if (extension == null) {
            return Collections.emptyList();
        }
        return extension.getTypes();
    }

    public List<Extension> getExtensions(String typeString) {
        Type type = typeMap.get(typeString);

        if (type == null) {
            return Collections.emptyList();
        }
        return type.getExtensions();
    }

    private void loadTypes(Context context) {
        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        BufferedReader reader = null;

        // Create the wildcard type ("*") that matches all types.
        createType(WILDCARD_STRING);
        try {
            is = assetManager.open(TYPE_FILE);
            reader = new BufferedReader(new InputStreamReader(is));
            String content = stripSurroundingChars(reader.readLine());
            String[] items = content.split(ITEM_SEPARATOR);

            for (String item : items) {
                item = stripSurroundingChars(item);
                String[] pair = item.split(PAIR_SEPARATOR);
                String extensionString = stripSurroundingChars(pair[0]);
                String typeString = stripSurroundingChars(pair[1]).replace("\\", "");

                addNewExtensionTypePair(extensionString, typeString);
            }
            Collections.sort(typeList);
        } catch (IOException exc) {
            Log.e(TAG, "Error reading mime types file.", exc);
        } finally {
            if (is != null) {
                try {
                    reader.close();
                } catch (IOException exc) {
                    // Do nothing.
                }
            }
        }
    }

    private void addNewExtensionTypePair(String extensionString, String typeString) {
        Extension extension = getExtension(extensionString);
        Type type = getType(typeString);

        extension.addType(type);
        type.addExtension(extension);
    }

    private Extension getExtension(String extensionString) {
        Extension extension = extensionMap.get(extensionString);

        if (extension == null) {
            extension = createExtension(extensionString);
        }
        return extension;
    }

    private Type getType(String typeString) {
        Type type = typeMap.get(typeString);

        if (type == null) {
            int separatorIndex = typeString.indexOf(TYPE_SEPARATOR);

            type = createType(typeString);
            if (separatorIndex > 0) {
                String mainType = typeString.substring(0, separatorIndex);
                String wildcardTypeString = mainType + TYPE_SEPARATOR + WILDCARD_STRING;
                Type wildcardType = typeMap.get(wildcardTypeString);

                if (wildcardType == null) {
                    createType(wildcardTypeString);
                }
            }
        }
        return type;
    }

    private Extension createExtension(String extensionString) {
        Extension extension = new Extension(extensionString);

        extensionMap.put(extensionString, extension);
        return extension;
    }

    private Type createType(String typeString) {
        Type type = new Type(typeString);

        typeMap.put(typeString, type);
        typeList.add(type);
        return type;
    }

    private static String stripSurroundingChars(String str) {
        return str.substring(1, str.length() - 1);
    }
}
