package com.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUID = 1L;
    // Unique identifier
    private String uuid;
    private String fullName;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);

    public Resume() {}

    public Resume(final String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(final String uuid, final String fullName) {
        Objects.requireNonNull(uuid, "uuid should not be null");
        Objects.requireNonNull(fullName, "fullName should not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, Section> getSections() {
        return sections;
    }

    public String getContact(final ContactType contactType) {
        return contacts.get(contactType);
    }

    public Section getSection(final SectionType sectionType) {
        return sections.get(sectionType);
    }

    public void addContact(final ContactType contactType, final String contact) {
        contacts.put(contactType, contact);
    }

    public void addSection(final SectionType sectionType, final Section section) {
        sections.put(sectionType, section);
    }

    public void addContacts(final Map<ContactType, String> contacts) {
        for (final Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            this.contacts.put(entry.getKey(), entry.getValue());
        }
    }

    public void addSections(final Map<SectionType, Section> sections) {
        for (final Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            this.sections.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) &&
                Objects.equals(fullName, resume.fullName) &&
                Objects.equals(contacts, resume.contacts) &&
                Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }

    @Override
    public String toString() {
        return String.format("%s(%s){%s, %s}", uuid, fullName, contacts, sections);
    }

    @Override
    public int compareTo(final Resume r) {
        final int cmp = this.fullName.compareTo(r.fullName);
        return cmp != 0 ? cmp : this.uuid.compareTo(r.uuid);
    }
}
