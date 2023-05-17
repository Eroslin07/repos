package com.newtouch.uctp.module.bpm.framework.flowable.core.validation.validator.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.validation.ValidationError;
import org.flowable.validation.validator.Problems;

/**
 * @author helong
 * @date 2023/5/17 17:42
 */
public class FlowElementValidator extends org.flowable.validation.validator.impl.FlowElementValidator {
    public FlowElementValidator() {
        super();
    }

    @Override
    protected void executeValidation(BpmnModel bpmnModel, Process process, List<ValidationError> errors) {
        for (FlowElement flowElement : process.getFlowElements()) {

            if (flowElement instanceof Activity) {
                Activity activity = (Activity) flowElement;
                super.handleConstraints(process, activity, errors);
                handleMultiInstanceLoopCharacteristics(process, activity, errors);
                super.handleDataAssociations(process, activity, errors);
            }

        }

    }

    @Override
    protected void handleMultiInstanceLoopCharacteristics(Process process, Activity activity, List<ValidationError> errors) {
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = activity.getLoopCharacteristics();
        if (activity instanceof UserTask) {
            return;
        }
        if (multiInstanceLoopCharacteristics != null) {

            if (StringUtils.isEmpty(multiInstanceLoopCharacteristics.getLoopCardinality())
                    && StringUtils.isEmpty(multiInstanceLoopCharacteristics.getInputDataItem()) && StringUtils.isEmpty(multiInstanceLoopCharacteristics.getCollectionString())) {

                addError(errors, Problems.MULTI_INSTANCE_MISSING_COLLECTION, process, activity, multiInstanceLoopCharacteristics,
                        "Either loopCardinality or loopDataInputRef/flowable:collection must been set");
            }

            if (!StringUtils.isEmpty(multiInstanceLoopCharacteristics.getCollectionString())) {

                if (multiInstanceLoopCharacteristics.getHandler() == null) {
                    // verify string parsing function attributes
                    addError(errors, Problems.MULTI_INSTANCE_MISSING_COLLECTION_FUNCTION_PARAMETERS, process, activity,
                            "The flowable:collection element string value requires the function parameters flowable:delegateExpression or flowable:class.");
                }
            }

        }
    }
}
