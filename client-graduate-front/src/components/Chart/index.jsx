import React, {useState} from 'react';
import Plot from 'react-plotly.js'
import {Button} from "antd";
import axios from "axios";
import URL from "../URL";
import Cookies from 'js-cookie'

const Chart = () => {

    const [centroidChart, setCentroidChart] = useState({
        x: [],
        y: [],
        z: []
    })

    const [dotsChart, setDotsChart] = useState({
        x: [],
        y: [],
        z: []
    })

    const onClick = async () =>{

        const idUser = localStorage.getItem('data');
        console.log(idUser);
        const {data: result} = await axios.get(URL.RESULT, {
            params: {
                idUser
            }
        });

        const centroidChart = {
            x: [],
            y: [],
            z: []
        };
        const dotsChart = {
            x: [],
            y: [],
            z: []
        };

        Object.entries(result).forEach(([key, value])=>{
            console.log('--->');

            const centroid = JSON.parse(key);


            centroidChart.x.push(centroid[0])
            centroidChart.y.push(centroid[1])
            centroidChart.z.push(centroid[2])

            console.log(centroidChart);
            value.forEach(e=>{
                dotsChart.x.push(e[0])
                dotsChart.y.push(e[1])
                dotsChart.z.push(e[2])
            })
        })


        setCentroidChart(centroidChart);
        setDotsChart(dotsChart);
    }
    return (
        <div>
            <Button type="primary" onClick={onClick}>
                Get result
            </Button>
            <Plot
                data={
                    [
                        {
                            x: centroidChart.x,
                            y: centroidChart.y,
                            z: centroidChart.z,
                            mode: 'markers',
                            type: 'scatter3d',
                            scene: 'scene1',
                            name: 'Centroid'
                        },
                        {
                            x: dotsChart.x,
                            y: dotsChart.y,
                            z: dotsChart.z,
                            mode: 'markers',
                            type: 'scatter3d',
                            scene: 'scene2',
                            name: 'Dots'
                        }
                    ]
                }
                layout={{
                    height: 800,
                    width: 1200,
                    title: `3D Views`,
                    scene1: {
                        domain: {
                            x: [0.00, 0.33],
                            y: [0.5, 1]
                        },
                        camera: {
                            center: {x: 0, y: 0, z: 0},
                            eye: {x: 2, y: 2, z: 0.1},
                            up: {x: 0, y: 0, z: 1}
                        }
                    },
                    scene2: {
                        domain: {
                            x: [0.33, 0.66],
                            y: [0.5, 1.0]
                        },
                        camera: {
                            center: {x: 0, y: 0, z: 0},
                            eye: {x: 0.1, y: 2.5, z: 0.1},
                            up: {x: 0, y: 0, z: 1}
                        }
                    }
                }}
            />
        </div>
    );
};

export default Chart;