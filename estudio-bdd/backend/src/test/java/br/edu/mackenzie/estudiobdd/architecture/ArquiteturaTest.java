package br.edu.mackenzie.estudiobdd.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages="br.edu.mackenzie.estudiobdd")
class ArquiteturaTest {
    @ArchTest static final ArchRule controllersNaoAcessamRepositorios=noClasses()
        .that().resideInAPackage("..controller..")
        .should().dependOnClassesThat().resideInAPackage("..repository..");
}
