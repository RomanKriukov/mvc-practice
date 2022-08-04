package lviv.cursor.demo.controller;

import lviv.cursor.demo.model.Person;
import lviv.cursor.demo.model.PersonForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

@Controller
@EnableWebMvc
public class PersonController {
    private static List<Person> persons = new ArrayList<Person>();

    static {
        persons.add(new Person(1, "Bill", "Gates", 70, "USA"));
        persons.add(new Person(2, "Steve", "Jobs", 50, "USA"));
    }

    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("message", "Hello world!");
        model.addAttribute("second_message", "Cursor forever!");

        return "index";
    }

    @RequestMapping(value = { "/personList" }, method = RequestMethod.GET)
    public String personList(Model model) {

        model.addAttribute("persons", persons);

        return "personList";
    }

    @RequestMapping(value = { "/addPerson" }, method = RequestMethod.GET)
    public String showAddPersonPage(Model model) {

        PersonForm personForm = new PersonForm();
        model.addAttribute("personForm", personForm);

        return "addPerson";
    }

    @RequestMapping(value = { "/addPerson" }, method = RequestMethod.POST)
    public String savePerson(Model model, //
                             @ModelAttribute("personForm") PersonForm personForm) {

        int id = personForm.getId();
        String firstName = personForm.getFirstName();
        String lastName = personForm.getLastName();
        int age = personForm.getAge();
        String address = personForm.getAddress();

        if (firstName != null && firstName.length() > 0 //
                && lastName != null && lastName.length() > 0) {
            Person newPerson = new Person(id, firstName, lastName, age, address);
            persons.add(newPerson);

            return "redirect:/personList";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addPerson";
    }

    @RequestMapping(value = { "/choosePerson" }, method = RequestMethod.GET)
    public String choosePersonForUpdate(Model model, @RequestParam String action) {

        model.addAttribute("persons", persons);
        model.addAttribute("action", action);

        return "choosePerson";
    }

    @RequestMapping(value = {"/update"}, method = RequestMethod.GET)
    public String updatePersonPage(Model model, @RequestParam int id){
        Person person = persons.stream().filter(p -> id == p.getId()).findFirst().orElse(null);
        PersonForm personForm = new PersonForm();
        personForm.setId(person.getId());
        personForm.setFirstName(person.getFirstName());
        personForm.setLastName(person.getLastName());
        personForm.setAge(person.getAge());
        personForm.setAddress(person.getAddress());
        model.addAttribute("personForm", personForm);
        return "updatePersonPage";
    }

    @RequestMapping(value = { "/updatePerson" }, method = RequestMethod.POST)
    public String updatePerson(Model model, //
                             @ModelAttribute("personForm") PersonForm personForm) {

        int id = personForm.getId();
        String firstName = personForm.getFirstName();
        String lastName = personForm.getLastName();
        int age = personForm.getAge();
        String address = personForm.getAddress();

        if (firstName != null && firstName.length() > 0 //
                && lastName != null && lastName.length() > 0) {
            Person newPerson = new Person(id, firstName, lastName, age, address);
            Person person = persons.stream().filter(p -> id == p.getId()).findFirst().orElse(null);
            int personId = persons.indexOf(person);
            persons.set(personId, newPerson);

            return "redirect:/personList";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "updatePersonPage";
    }

    @RequestMapping(value = {"/delete"}, method = RequestMethod.GET)
    public String deletePerson(Model model, @RequestParam int id){
        Person person = persons.stream().filter(p -> id == p.getId()).findFirst().orElse(null);
        persons.remove(person);
        return "redirect:/personList";
    }

    @RequestMapping(value = { "/info" }, method = RequestMethod.GET)
    public String getInfo(Model model) {



        return "info";
    }
}
