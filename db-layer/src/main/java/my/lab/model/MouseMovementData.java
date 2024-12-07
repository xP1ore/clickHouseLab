package my.lab.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MouseMovementData {

    private int x;
    private int y;
    private int deltaX;
    private int deltaY;
    private float clientTimeStamp;
    private byte button;
    private String target;
    
}
