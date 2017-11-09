package graphing;

import org.junit.Test;
import scheduleCreator.model.Course;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by Oscar Reyes on 7/24/2017.
 */
public class ClusterPickerTest {
    @Test
    public void chooseSectionNodes() throws Exception {
        HashMap<Course, String[]> sectionsOfCourse = getSectionsOfCourses();
        System.out.print(sectionsOfCourse);
        // Connect sections that are linked

        // Do clustering
        // Test the cluster picker
    }

    private HashMap<Course, String[]> getSectionsOfCourses() {
        HashMap<Course, String[]> sectionsOfCourse = new HashMap<>();
        Course c1 = makeCourse(new String[]{"Calculus II", "MATH", "2414"});
        Course c2 = makeCourse(new String[]{"University Physics I", "PHYS", "2325"});
        Course c3 = makeCourse(new String[]{"University Physics I Lab", "PHYS", "2125"});
        Course c4 = makeCourse(new String[]{"Government and Politics of US", "POLS", "2301"});
        Course c5 = makeCourse(new String[]{"American History since 1877", "HIST", "1302"});
        Course c6 = makeCourse(new String[]{"Objt Oriented Software Enginrg", "CSEN", "2310"});

        sectionsOfCourse.put(c1 ,getSections("testFiles/calculusII.txt"));
        sectionsOfCourse.put(c2 ,getSections("testFiles/csen.txt"));
        sectionsOfCourse.put(c3 ,getSections("testFiles/history.txt"));
        sectionsOfCourse.put(c4 ,getSections("testFiles/physics.txt"));
        sectionsOfCourse.put(c5 ,getSections("testFiles/physicsLab.txt"));
        sectionsOfCourse.put(c6 ,getSections("testFiles/politicalScience.txt"));

        return sectionsOfCourse;
    }

    private Course makeCourse(String[] courseParameters) {
        return new Course.CourseBuilder(courseParameters[0])
                .courseDepartment(courseParameters[1])
                .courseNumber(courseParameters[2])
                .build();
    }

    private String[] getSections(String fileName) {
        List<String> sectionLines = getLinesFromFile(fileName, "SR");
        String[] sections = new String[sectionLines.size()];
        sections = sectionLines.toArray(sections);
        return sections;
    }

    private List<String> getLinesFromFile(String fileName, String filter) {
        System.out.print(fileName + "\n");
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            return stream
                    .filter(line -> line.startsWith(filter))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private HashMap<String, List<String>> getAllSectionsWithClasses() {
        HashMap<String, List<String>> linksOfSection = new HashMap<>();
        String[] sectionFiles = {
                "testFiles/calculusII.txt", "testFiles/csen.txt",
                "testFiles/history.txt", "testFiles/physics.txt",
                "testFiles/physicsLab.txt", "testFiles/politicalScience.txt"
        };
        for (String file : sectionFiles) {
            HashMap<String, List<String>> tmp = getLinkedSections(file);
            linksOfSection.putAll(tmp);
        }
        return linksOfSection;
    }

    private HashMap<String, List<String>> getLinkedSections(String fileName) {
        HashMap<String, List<String>> linksOfCrn = new HashMap<>();
        List<String> lines = getLinesFromFile(fileName, "Instructor");
        for (String line: lines) {
            List<String> matches = getAllCrnMatches(line);
            List<String> links = matches.subList(1, matches.size() - 1);
            linksOfCrn.put(matches.get(0), links);
        }
        return linksOfCrn;
    }

    private List<String> getAllCrnMatches(String line) {
        List <String> crnMatches = new ArrayList<>();
        Pattern p = Pattern.compile("\\d{5}");
        Matcher m = p.matcher(line);

        while (m.find()) {
            String group = m.group();
            crnMatches.add(group);
        }
        return crnMatches;
    }
}