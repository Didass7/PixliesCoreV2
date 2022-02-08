package net.pixlies.nations.nations.customization;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum NationConstitution {

    /*
     * Values:
     * 0 - No
     * 1 - Restricted
     * 2 - Yes
     */

    ALCOHOL_CONSUMPTION(2),
    FIREARM_POSSESSION(1),
    DEMOCRACY(2),
    EQUALITY(2),
    HABEAS_CORPUS(2),

    FREEDOM_OF_SPEECH(2),
    FREEDOM_OF_THE_PRESS(2),
    FREEDOM_OF_RELIGION(2),
    PRIVATE_PROPERTY(2),
    RIGHT_TO_LIBERTY(1);

    @Getter final int defaultValue;

}
