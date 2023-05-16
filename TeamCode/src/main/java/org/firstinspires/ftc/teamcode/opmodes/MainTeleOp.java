package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

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

    enum FOREARMSTATE {
        down,
        up,
        coneright
    }

    enum WRISTSTATE {
        counterClockWise,
        clockWise
    }

    int numOfGamepads = 1;
    @Override
    public void runOpMode() throws InterruptedException {
        Gamepad lastG1 = new Gamepad();
        Gamepad lastG2 = new Gamepad();


        while(opModeIsActive()) {
            setNumOfGamepads();

            setLastGamepads(lastG1, lastG2);
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
}
