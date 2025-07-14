import { useEffect} from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";


const AuthCallback = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const { setAccessToken } = useAuth();

    useEffect(() => {
        const token = searchParams.get("access_token");

        if (token) {
            setAccessToken(token);
            localStorage.setItem("accessToken", token);
            navigate("/dashboard");
        } else {
            console.error("No access token found in the URL");
            navigate("/login");
        }
    }, [searchParams, setAccessToken, navigate]);

    return (
        <div>
            <h1>Authenticating...</h1>
            <p>Please wait while we log you in.</p>
        </div>
    );
};

export default AuthCallback;