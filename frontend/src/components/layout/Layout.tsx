import React, { ReactNode } from "react";
import classes from "./Layout.module.scss";

const Layout = ({ children }: { children: ReactNode }) => {
  return (
    <div className={classes.root}>
      <header className={classes.container}>
        <a href="/">
            <img className={classes.logo} src="/logo.svg" alt="logo" />
        </a>
      </header>
      <main className={classes.container}>{children}</main>
      <footer>
        <ul className={classes.container}>
            <li>
                <img src="/logo-dark.svg" alt="dart logo" />
                <span>2025</span>
            </li>
            <li>
                <a href="">About</a>
            </li>
            <li>
                <a href="">Accessibility</a>
            </li>
            <li>
                <a href="">User Agreement</a>
            </li>
            <li>
                <a href="">Private Policy</a>
            </li>
            <li>
                <a href="">Cookie Policy</a>
            </li>
            <li>
                <a href="">Copyright Policy</a>
            </li>
            <li>
                <a href="">Brand Policy</a>
            </li>
            <li>
                <a href="">Guest Controls</a>
            </li>
            <li>
                <a href="">Community Guidelines</a>
            </li>
            <li>
                <a href="">Language</a>
            </li>
        </ul>
      </footer>
    </div>
  );
};

export default Layout;
