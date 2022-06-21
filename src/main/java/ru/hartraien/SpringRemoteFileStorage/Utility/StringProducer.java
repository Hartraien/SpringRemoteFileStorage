package ru.hartraien.SpringRemoteFileStorage.Utility;

/**
 * Produces string of given length
 */
public interface StringProducer
{
    /**
     * Produce string of given length
     *
     * @param length - length of string
     * @return string of given length
     */
    String getString( int length );
}
