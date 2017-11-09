package scheduleCreator.model;

import graphing.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Oscar Reyes on 7/14/2017.
 */

class SectionToNodeConverter {
    private HashMap<Course, String[]> sectionsOfCourse;

    SectionToNodeConverter(HashMap<Course, String[]> sectionsOfCourse) {
        this.sectionsOfCourse = sectionsOfCourse;
    }

    List<Node> getNodes() {
        Set<Map.Entry<Course, String[]>> entries = sectionsOfCourse.entrySet();
        Iterator iterator = entries.iterator();

        List<Node> allNodes = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            List<Node> nodes = createNodes(pair);
            allNodes.addAll(nodes);
            iterator.remove();
        }
        return allNodes;
    }

    private List<Node> createNodes(Map.Entry pair) {
        Course course = (Course) pair.getKey();
        String[] sectionStrings = (String[]) pair.getValue();
        List<CourseSection> sections = getCourseSections(course, sectionStrings);
        course.setSectionCount(sections.size());
        return getNodesFromSections(sections);
    }

    private List<CourseSection> getCourseSections(Course course, String[] sectionStrings) {
        return Arrays.stream(sectionStrings)
                .map(sectionString -> convertToCourseSection(course, sectionString))
                .collect(Collectors.toList());
    }

    private CourseSection convertToCourseSection(Course course, String sectionString) {
        SectionExtractor converter =  new SectionExtractor(course, sectionString);
        return converter.createCourseSection();
    }

    private List<Node> getNodesFromSections(List<CourseSection> sections) {
        return sections.stream()
                .map(section -> getCourse(section))
                .collect(Collectors.toList());
    }

    private Node getCourse(CourseSection section) {
        Course course = section.getCourse();
        Double sectionCount = course.getSectionCount();
        return new Node(section, sectionCount);
    }
}