package com.datenotes.data;

import com.datenotes.data.List;
import com.datenotes.data.Note;

import org.junit.Test;

import static com.datenotes.data.List.DEFAULT_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListTest {

    @Test
    public void emptyConstructor() throws Exception {
        List list = new List();
        assertThat(list.getName(), is(DEFAULT_NAME));
    }

    @Test
    public void constructorWithName() throws Exception {
        String name = "My list 33";
        List list = new List(name);
        assertThat(list.getName(), is(name));
    }

    @Test
    public void emptyEqualsTest() throws Exception {
        List list1 = new List();
        List list2 = new List();
        assertThat(list1, is(list2));
    }

    @Test
    public void equalsTest() throws Exception {
        Note note = new Note("Message");
        List list1 = new List();
        list1.addNote(note);
        List list2 = new List();
        list2.addNote(note);
        assertThat(list1, is(list2));

        list1.addNote(note);
        assertThat(list1, not(list2));
    }
}
