import './App.css';
import Container from "./components/Container";
import ConnectToDevice from "./components/ConnectToDevice";
import Chart from "./components/Chart";
import {Divider} from "antd";

function App() {
    return (
        <Container>
            <ConnectToDevice/>
            <Divider />
            <Chart/>
        </Container>
    );
}

export default App;
