package com.project.api.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
        packages = "com.project.api",
        importOptions = {ImportOption.DoNotIncludeTests.class}
)
class LayerDependencyArchitectureTest {

    @ArchTest
    static final ArchRule domain_must_not_depend_on_spring_web_or_jpa =
            noClasses()
                    .that().resideInAnyPackage("com.project.api.domain..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "org.springframework..",
                            "org.hibernate..",
                            "jakarta.persistence..",
                            "jakarta.servlet.."
                    )
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule application_must_not_depend_on_presentation_or_infrastructure =
            noClasses()
                    .that().resideInAnyPackage("com.project.api.application..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "com.project.api.presentation..",
                            "com.project.api.infrastructure..",
                            "jakarta.servlet..",
                            "org.springframework.web.."
                    )
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule presentation_must_not_depend_on_infrastructure =
            noClasses()
                    .that().resideInAnyPackage("com.project.api.presentation..")
                    .should().dependOnClassesThat().resideInAnyPackage("com.project.api.infrastructure..")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule bootstrap_is_the_only_layer_that_may_depend_on_config_packages =
            noClasses()
                    .that().resideInAnyPackage(
                            "com.project.api.domain..",
                            "com.project.api.application..",
                            "com.project.api.presentation..",
                            "com.project.api.infrastructure.."
                    )
                    .should().dependOnClassesThat().resideInAnyPackage("com.project.api.config..")
                    .allowEmptyShould(true);
}
