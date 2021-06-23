import React from 'react'
import {Card} from 'antd'

const Container = (props) => {
    return (
        <Card {...props} style={{borderRadius: 6, ...props.style}}>
            {props.children}
        </Card>
    )
}

export default Container
