import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import { useState } from "react";
import { registerUser } from "../services/UserService";
import { Spinner } from "react-bootstrap";

const Register = () => {
	// El hook que estaremos usandos
	// Lo inicializaremos vacíos
	const [name, setName] = useState(""); // Para obtener el nombre o settearlo
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [errors, setErrors] = useState<any>({});
	const [sendingData, setSendingData] = useState(false);

	// Es una promesa porque estamos el método de register envía un petición post, por eso se debe poner asyc
	const register = async (e: React.SyntheticEvent) => {
		e.preventDefault(); // Evitamos que se envié el formulario
		try {
			setSendingData(true);
			await registerUser(name, email, password); // Le decimos que espere la respuesta

			// Redireccionar el usuario al panel
			setSendingData(false);
		} catch (errors: any) {
			setErrors(errors.response.data.errors); // Obtenemos los errores obtenidos
			setSendingData(false);
		}
	};

	return (
		<Container>
			<Row>
				<Col lg="5" md="10" sm="10" className="mx-auto">
					<Card className="mt-5">
						<Card.Body>
							<h4>Crear cuenta</h4> <hr />
							<Form onSubmit={register}>
								<Form.Group className="mb-3" controlId="name">
									{/* Siempre agregar el controlId */}
									<Form.Label>Nombre</Form.Label>
									<Form.Control
										isInvalid={
											!!errors?.name
										} /* Si existe errors?.name mostramos error, los signos de admiración son para castear a boolean osea retornara un true o false*/
										value={name} // Le decimos que el valor será el nombre declarado al inicio
										onChange={e => setName(e.target.value)} // Le decimos que al detectar un cambio le mandará el nuevo valor a nombre
										type="text"
										placeholder="ej. Juan Perez"
									></Form.Control>

									<Form.Control.Feedback type="invalid">
										{errors?.name}
									</Form.Control.Feedback>
								</Form.Group>
								<Form.Group className="mb-3" controlId="email">
									<Form.Label>Correo electrónico</Form.Label>

									<Form.Control
										isInvalid={!!errors?.email}
										value={email}
										onChange={e => {
											setEmail(e.target.value);
										}}
										type="email"
										placeholder="ej. ejemplo@gmail.com"
									></Form.Control>

									<Form.Control.Feedback type="invalid">
										{errors?.email}
									</Form.Control.Feedback>
								</Form.Group>
								<Form.Group className="mb-3" controlId="password">
									<Form.Label>Password</Form.Label>

									<Form.Control
										isInvalid={!!errors?.password}
										value={password}
										onChange={e => {
											setPassword(e.target.value);
										}}
										type="password"
										placeholder="**************"
									></Form.Control>

									<Form.Control.Feedback type="invalid">
										{errors?.password}
									</Form.Control.Feedback>
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
											<span> Registrando...</span>
										</>
									) : (
										<>Crear cuenta</>
									)}
								</Button>
							</Form>
						</Card.Body>
					</Card>
				</Col>
			</Row>
		</Container>
	);
};

export default Register;
