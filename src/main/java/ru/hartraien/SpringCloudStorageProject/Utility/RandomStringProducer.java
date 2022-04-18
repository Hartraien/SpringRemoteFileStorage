package ru.hartraien.SpringCloudStorageProject.Utility;

import java.util.concurrent.ThreadLocalRandom;

public class RandomStringProducer implements StringProducer
{
    private final static int LEFT_LIMIT = 97; // letter 'a'
    private final static int RIGHT_LIMIT = 122; // letter 'z'

    @Override
    public String getString( int length )
    {
        return ThreadLocalRandom.current().ints( LEFT_LIMIT, RIGHT_LIMIT + 1 )
                .limit( length )
                .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                .toString();
    }
}
