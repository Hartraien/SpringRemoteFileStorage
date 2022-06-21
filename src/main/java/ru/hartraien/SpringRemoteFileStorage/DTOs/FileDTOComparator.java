package ru.hartraien.SpringRemoteFileStorage.DTOs;

import java.util.Comparator;

public class FileDTOComparator implements Comparator<FileDTO>
{
    @Override
    public int compare( FileDTO o1, FileDTO o2 )
    {
        if ( o1.isDirectory() && !o2.isDirectory() )
            return -1;
        if ( !o1.isDirectory() && o2.isDirectory() )
            return 1;
        return o1.getFileName().compareTo( o2.getFileName() );
    }
}
