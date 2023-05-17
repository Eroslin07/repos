package com.newtouch.uctp.module.bpm.framework.flowable.core.validation;

import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorImpl;
import org.flowable.validation.validator.ValidatorSet;
import org.flowable.validation.validator.impl.ServiceTaskValidator;

/**
 * @author helong
 * @date 2023/5/17 16:41
 */
public class ProcessValidatorFactory extends org.flowable.validation.ProcessValidatorFactory {
    public ProcessValidatorFactory() {
        super();
    }

    @Override
    public ProcessValidator createDefaultProcessValidator() {
        ProcessValidatorImpl processValidator = new ProcessValidatorImpl();

        ValidatorSet executableProcessValidatorSet = new com.newtouch.uctp.module.bpm.framework.flowable.core.validation.validator.ValidatorSetFactory().createFlowableExecutableProcessValidatorSet();

        if (super.customServiceTaskValidator != null) {
            executableProcessValidatorSet.removeValidator(ServiceTaskValidator.class);
            executableProcessValidatorSet.addValidator(super.customServiceTaskValidator);
        }

        processValidator.addValidatorSet(executableProcessValidatorSet);

        return processValidator;
    }
}
