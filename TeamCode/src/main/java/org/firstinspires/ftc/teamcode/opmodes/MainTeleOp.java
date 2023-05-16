package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;


/*
Controls:

release everything: retracted state
a : release cone/intake
b : low height
x : medium height
y : high height
right trigger (hold) : turret to the right
left trigger (hold) : turret to the left
left joystick : strafe (field centric)
right joystick : turn
left joystick (press) : cone righting + snail drive

 */
public class MainTeleOp extends LinearOpMode {
    enum SLIDESTATE {
        zero,
        low,
        medium,
        high,
        coneright,
        manual
    }

    enum TURRETSTATE {
        center,
        right,
        left,
    }

    enum ELBOWSTATE {
        down,
        up,
        coneright
    }

    enum WRISTSTATE {
        counterClockWise,
        clockWise,
        straight
    }

    SLIDESTATE slideState;
    TURRETSTATE turretState;
    ELBOWSTATE elbowState;
    WRISTSTATE wristState;

    int numOfGamepads = 1;
    @Override
    public void runOpMode() throws InterruptedException {
        Gamepad lastGamepad1 = new Gamepad();
        Gamepad lastGamepad2 = new Gamepad();
        initStates();


        while(opModeIsActive()) {
            setNumOfGamepads();

            if(gamepad1.a && !lastGamepad1.a) {
                slideState = SLIDESTATE.zero;
            }

            setLastGamepads(lastGamepad1, lastGamepad2);
        }
    }

    public void setLastGamepads(Gamepad lastG1, Gamepad lastG2) {
        lastG1.copy(gamepad1);
        lastG2.copy(gamepad2);
    }

    public void setNumOfGamepads() {
        if(gamepad2.getGamepadId() == -1) {
            numOfGamepads = 1;
        } else {
            numOfGamepads = 2;
        }
    }

    public void initStates() {
        slideState = SLIDESTATE.zero;
        turretState = TURRETSTATE.center;
        elbowState = ELBOWSTATE.up;
        wristState = WRISTSTATE.straight;
    }
}
