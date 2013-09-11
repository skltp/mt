package se.skltp.messagebox.web.spec

class Config {

    String property
    String value

    void execute() {
        System.setProperty(property, value)
    }
}
