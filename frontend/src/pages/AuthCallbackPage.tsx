import { useEffect } from "react";
import {
    useNavigate,
    useSearchParams
} from "react-router-dom";

import {
    Box,
    CircularProgress,
    Typography,
    Container
} from '@mui/material';

import { useAuth } from "../context";

const AuthCallbackPage = () => {
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
        <Container maxWidth="sm">
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    minHeight: '50vh',
                    textAlign: 'center'
                }}
            >
                <Box sx={{ position: 'relative', display: 'inline-flex', mb: 2 }}>
                    <CircularProgress size={100} thickness={6} />
                </Box>
                <Typography variant="h6" component="h1" gutterBottom>
                    Authenticating
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    Please wait while we log you in...
                </Typography>
            </Box>
        </Container>
    );
};

export default AuthCallbackPage;