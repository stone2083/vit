package org.jellylab.vit;

/**
 * @author jinli Mar 24, 2015
 */
public interface VitServer {

    void init() throws Exception;

    void start() throws Exception;

    void shutdown() throws Exception;

}
