package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hardware6417 {
    DcMotorEx slider, auxSlider, frontLeft, frontRight, backLeft, backRight;
    Servo turret, elbow, wrist, grabber;

    public Hardware6417(HardwareMap hwMap) {
        initSlides(hwMap);
        initIntake(hwMap);
    }

    public void initSlides(HardwareMap hwMap) {
        slider  = hwMap.get(DcMotorEx.class, "Slider");
        auxSlider = hwMap.get(DcMotorEx.class, "AuxSlider");

        slider.setDirection(DcMotorSimple.Direction.REVERSE);
        auxSlider.setDirection(DcMotorSimple.Direction.REVERSE);

        //set all motors to zero power
        slider.setPower(0);
        auxSlider.setPower(0);

        //set brake behavior
        slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        auxSlider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void initIntake(HardwareMap hwMap) {
        turret      = hwMap.get(Servo.class, "Turret");
        grabber     = hwMap.get(Servo.class, "Grabber");
        wrist       = hwMap.get(Servo.class, "Wrist");
    }
}
