package com.infybuzz.listener;

import com.infybuzz.model.StudentDTO;
import org.springframework.batch.core.SkipListener;

public class SkipListenerImpl implements SkipListener<StudentDTO, StudentDTO> {
    @Override
    public void onSkipInRead(Throwable t) {

    }

    @Override
    public void onSkipInWrite(StudentDTO item, Throwable t) {

    }

    @Override
    public void onSkipInProcess(StudentDTO item, Throwable t) {

    }
}
