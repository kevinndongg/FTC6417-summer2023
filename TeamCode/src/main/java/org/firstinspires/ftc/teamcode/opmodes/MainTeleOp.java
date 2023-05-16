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
    @Override
    public void runOpMode() throws InterruptedException {
        Gamepad lastG1 = new Gamepad();
        Gamepad lastG2 = new Gamepad();

        while(opModeIsActive()) {
            

            lastG1.copy(gamepad1);
            lastG2.copy(gamepad2);
        }
    }
}
