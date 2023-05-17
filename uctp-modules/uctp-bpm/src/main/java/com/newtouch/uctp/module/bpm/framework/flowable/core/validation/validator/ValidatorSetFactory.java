package com.newtouch.uctp.module.bpm.framework.flowable.core.validation.validator;

import org.flowable.validation.validator.ValidatorSet;
import org.flowable.validation.validator.ValidatorSetNames;
import org.flowable.validation.validator.impl.*;

/**
 * @author helong
 * @date 2023/5/17 17:41
 */
public class ValidatorSetFactory extends org.flowable.validation.validator.ValidatorSetFactory {
    public ValidatorSetFactory() {
        super();
    }

    @Override
    public ValidatorSet createFlowableExecutableProcessValidatorSet() {
        ValidatorSet validatorSet = new ValidatorSet(ValidatorSetNames.FLOWABLE_EXECUTABLE_PROCESS);

        validatorSet.addValidator(new AssociationValidator());
        validatorSet.addValidator(new SignalValidator());
        validatorSet.addValidator(new OperationValidator());
        validatorSet.addValidator(new ErrorValidator());
        validatorSet.addValidator(new DataObjectValidator());

        validatorSet.addValidator(new BpmnModelValidator());
        validatorSet.addValidator(new com.newtouch.uctp.module.bpm.framework.flowable.core.validation.validator.impl.FlowElementValidator());

        validatorSet.addValidator(new StartEventValidator());
        validatorSet.addValidator(new SequenceflowValidator());
        validatorSet.addValidator(new UserTaskValidator());
        validatorSet.addValidator(new ServiceTaskValidator());
        validatorSet.addValidator(new ScriptTaskValidator());
        validatorSet.addValidator(new SendTaskValidator());
        validatorSet.addValidator(new ExclusiveGatewayValidator());
        validatorSet.addValidator(new EventGatewayValidator());
        validatorSet.addValidator(new SubprocessValidator());
        validatorSet.addValidator(new EventSubprocessValidator());
        validatorSet.addValidator(new BoundaryEventValidator());
        validatorSet.addValidator(new IntermediateCatchEventValidator());
        validatorSet.addValidator(new IntermediateThrowEventValidator());
        validatorSet.addValidator(new MessageValidator());
        validatorSet.addValidator(new EventValidator());
        validatorSet.addValidator(new EndEventValidator());

        validatorSet.addValidator(new ExecutionListenerValidator());
        validatorSet.addValidator(new FlowableEventListenerValidator());

        validatorSet.addValidator(new DiagramInterchangeInfoValidator());

        return validatorSet;
    }
}
