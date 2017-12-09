package jonl.ge;

public abstract class Property extends Component {

    public abstract void create();
    
    public abstract void update();
    
    @Override
    final void updateComponent() {
        this.update();
    }
    
}