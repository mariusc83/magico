package org.mariusconstantin.annotations;

/**
 * Created by Marius on 12/11/2015.
 */
public @interface POST {
    String url();
    String[] keys();
    String[] params();
}
