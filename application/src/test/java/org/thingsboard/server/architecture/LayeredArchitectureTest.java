package org.thingsboard.server.architecture;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class LayeredArchitectureTest {
    private static final JavaClasses CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("org.thingsboard.server");

    @Test
    public void controllers_should_not_depend_on_transport() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..transport..");

        rule.check(CLASSES);
    }

    @Test
    public void services_should_not_depend_on_controllers_except_whitelist() {
        DescribedPredicate<JavaClass> isController =
                JavaClass.Predicates.resideInAPackage("..controller..");

        DescribedPredicate<JavaClass> isWhitelisted =
                JavaClass.Predicates.simpleName(
                        "HttpValidationCallback"
                ).or(JavaClass.Predicates.simpleName("BaseController"));

        DescribedPredicate<JavaClass> forbiddenControllerTargets =
                isController.and(DescribedPredicate.not(isWhitelisted));

        ArchRule rule = noClasses()
                .that().resideInAPackage("..service..")
                .should().dependOnClassesThat(forbiddenControllerTargets);

        rule.check(CLASSES);
    }
}