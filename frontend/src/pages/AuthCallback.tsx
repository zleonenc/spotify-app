import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

import { useAuth } from "../context";

const AuthCallback = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const { setUserId } = useAuth();

    useEffect(() => {
        const userId = searchParams.get("user_id");

        if (userId) {
            setUserId(userId);
            localStorage.setItem("userId", userId);
            navigate("/dashboard");
        } else {
            console.error("No user ID found in the URL");
            navigate("/login");
        }
    }, [searchParams, setUserId, navigate]);

    return (
        <div>
            <h1>Authenticating...</h1>
            <p>Please wait while we log you in.</p>
        </div>
    );
};

export default AuthCallback;