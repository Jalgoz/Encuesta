import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import Alert from "react-bootstrap/Alert";
import { useState } from "react";
import { Spinner } from "react-bootstrap";
import { loginUser } from "../services/UserService";

const Login = () => {
	// El hook que estaremos usandos
	// Lo inicializaremos vacíos
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [error, setError] = useState("");
	const [sendingData, setSendingData] = useState(false);

	// Es una promesa porque estamos el método de register envía un petición post, por eso se debe poner asyc
	const login = async (e: React.SyntheticEvent) => {
		e.preventDefault(); // Evitamos que se envié el formulario
		try {
			setSendingData(true);
			setError("");
			const rest = await loginUser(email, password); // Obtenemos el token del login
			const token = rest.data.token;
			// Redireccionar el usuario al panel
			setSendingData(false);
		} catch (errors: any) {
			if (errors.response) {
				errors.response.status === 403 &&
					setError("Las credenciales son incorrectas");
			}

			setSendingData(false);
		}
	};

	return (
		<Container>
			<Row>
				<Col lg="5" md="10" sm="10" className="mx-auto">
					<Card className="mt-5">
						<Card.Body>
							<h4>Iniciar sesión</h4> <hr />
							<Form onSubmit={login}>
								<Form.Group className="mb-3" controlId="email">
									<Form.Label>Correo electrónico</Form.Label>

									<Form.Control
										value={email}
										onChange={e => {
											setEmail(e.target.value);
										}}
										type="email"
										placeholder="ej. ejemplo@gmail.com"
									></Form.Control>
								</Form.Group>
								<Form.Group className="mb-3" controlId="password">
									<Form.Label>Password</Form.Label>

									<Form.Control
										value={password}
										onChange={e => {
											setPassword(e.target.value);
										}}
										type="password"
										placeholder="**************"
									></Form.Control>
								</Form.Group>
								<Button type="submit">
									{sendingData ? (
										<>
											<Spinner
												animation="border"
												as="span"
												size="sm"
												role="status"
												aria-hidden="true"
											></Spinner>
											&nbsp;
											<span> Iniciando sesión...</span>
										</>
									) : (
										<>Iniciar sesión</>
									)}
								</Button>
							</Form>
							<Alert className="mt-4" show={!!error} variant="danger">
								{error}
							</Alert>
						</Card.Body>
					</Card>
				</Col>
			</Row>
		</Container>
	);
};

export default Login;
