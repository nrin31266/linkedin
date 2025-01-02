
import Box from "../../../../components/Box/Box";
import Button from "../../../../components/Button/Button";
import Input from "../../../../components/Input/Input";
import Layout from "../../../../components/layout/Layout";
import classes from "./Register.module.scss";

const Register = () => {
  return (
    <Layout>
      <div className={classes.root}>
        <Box>
            <h1>Register</h1>
            <p>Make the most of your professional life.</p>
            <form>
                <Input type="email" id="email" label="Email" />
                <Input type="password" id="email" label="Password" />
                <Button style={{width: '100%'}} type="submit">Agree & Join</Button>
            </form>
        </Box>
      </div>
    </Layout>
  );
};

export default Register;
