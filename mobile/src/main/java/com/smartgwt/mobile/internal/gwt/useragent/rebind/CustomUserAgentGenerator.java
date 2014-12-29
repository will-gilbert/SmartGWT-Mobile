package com.smartgwt.mobile.internal.gwt.useragent.rebind;

import java.io.PrintWriter;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.SelectionProperty;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.smartgwt.mobile.SGWTInternal;

@SGWTInternal
public class CustomUserAgentGenerator extends Generator {

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
        final TypeOracle typeOracle = context.getTypeOracle();

        final JClassType userType;
        try {
            userType = typeOracle.getType(typeName);
        } catch (NotFoundException ex) {
            logger.log(TreeLogger.ERROR, "Unable to find metadata for type: " + typeName, ex);
            throw new UnableToCompleteException();
        }

        final PropertyOracle propertyOracle = context.getPropertyOracle();

        boolean userAgentRuntimeWarningValue = true;
        try {
            ConfigurationProperty property = propertyOracle.getConfigurationProperty("user.agent.runtimeWarning");
            userAgentRuntimeWarningValue = Boolean.valueOf(property.getValues().get(0));
        } catch (BadPropertyValueException ex) {
            logger.log(TreeLogger.WARN, "Unable to find value for 'user.agent.runtimeWarning'", ex);
        }

        final String userAgentValue;
        final SelectionProperty selectionProperty;
        try {
            selectionProperty = propertyOracle.getSelectionProperty(logger, "user.agent");
            userAgentValue = selectionProperty.getCurrentValue();
        } catch (BadPropertyValueException ex) {
            logger.log(TreeLogger.ERROR, "Unable to find value for 'user.agent'", ex);
            throw new UnableToCompleteException();
        }

        final String packageName = userType.getPackage().getName(),
                className = userType.getName().replace('.', '_') + "Impl" + CustomUserAgentPropertyGenerator.ucfirst(userAgentValue);

        final ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, className);
        composerFactory.addImplementedInterface(userType.getQualifiedSourceName());

        PrintWriter pw = context.tryCreate(logger, packageName, className);
        if (pw != null) {
            SourceWriter sw = composerFactory.createSourceWriter(context, pw);

            sw.println();
            sw.println("public boolean getUserAgentRuntimeWarning() {");
            sw.println("    return " + userAgentRuntimeWarningValue + ";");
            sw.println("}");
            sw.println();

            sw.println();
            sw.println("public native String getRuntimeValue() /*-{");
            sw.indent();
            CustomUserAgentPropertyGenerator.writeUserAgentDetectionJavaScript(sw, selectionProperty.getPossibleValues());
            sw.outdent();
            sw.println("}-*/;");
            sw.println();

            sw.println();
            sw.println("public String getCompileTimeValue() {");
            sw.println("    return \"" + userAgentValue.trim() + "\";");
            sw.println("}");

            sw.commit(logger);
        }
        return composerFactory.getCreatedClassName();
    }
}
