import { ButtonHTMLAttributes } from "react";
import classes from "./Button.module.scss";

type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  outline?: boolean;
};

const Button = ({ outline, ...others }: ButtonProps) => {
  return (
    <button {...others} className={`${classes.root} ${outline ? classes.outline : ""}`}>
      Button
    </button>
  );
};

export default Button;
